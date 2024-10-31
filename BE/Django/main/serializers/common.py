from rest_framework import serializers

class Metadata(serializers.Serializer):
    title = serializers.CharField()
    author = serializers.CharField()
    cover_alt = serializers.CharField(required=False)
    cover = serializers.ImageField()
    created_at = serializers.DateTimeField()